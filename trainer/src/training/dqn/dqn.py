import torch
from torch import nn


class DQN(nn.Module):
    def __init__(self, n_observations: int, n_actions: int):
        super(DQN, self).__init__()
        self.n_actions = n_actions
        self.layer1 = nn.Linear(n_observations, 128)
        self.layer2 = nn.Linear(128, 128)
        self.layer3 = nn.Linear(128, n_actions * 2)
        self.relu = nn.ReLU()

    # Called with either one element to determine next action, or a batch
    # during optimization. Returns tensor([[left0exp,right0exp]...]).
    def forward(self, x: torch.Tensor):
        x = self.relu(self.layer1(x))
        x = self.relu(self.layer2(x))
        batch_size = x.shape[0]
        return self.layer3(x).reshape(
            batch_size, 2, self.n_actions
        )  # (batch_size, n_actions * 2) => (batch_size, n_actions, 2)
